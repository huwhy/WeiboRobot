package cn.huwhy.weibo.robot.action;

import cn.huwhy.common.util.CollectionUtil;
import cn.huwhy.common.util.ThreadUtil;
import cn.huwhy.weibo.robot.model.Member;
import cn.huwhy.weibo.robot.model.MyFans;
import cn.huwhy.weibo.robot.model.Task;
import cn.huwhy.weibo.robot.model.TaskStatus;
import cn.huwhy.weibo.robot.model.Word;
import cn.huwhy.weibo.robot.model.WordType;
import cn.huwhy.weibo.robot.service.FansService;
import cn.huwhy.weibo.robot.service.MemberService;
import cn.huwhy.weibo.robot.service.TaskService;
import cn.huwhy.weibo.robot.service.WordService;
import org.apache.commons.lang.time.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommentAction {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static String uri = "https://weibo.com/comment/inbox?topnav=1&wvr=6&f=1";
    private static Pattern timePattern = Pattern.compile("\\d{1,2}月10日\\s\\d{2}:\\d{2}");

    private WebDriver driver;
    private Member member;
    private Date minDate;
    private volatile boolean running = false;
    private long lastCommentId;
    private long curMaxCommentId;
    private List<Word> words;
    private Set<Word> hitWords = new HashSet<>();
    private Map<Long, MyFans> fansMap = new HashMap<>();

    @Autowired
    private FansService fansService;
    @Autowired
    private WordService wordService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private TaskService taskService;

    private String summaryFormat = "处理%s条评论，发现删除%s条黑评论";

    public CommentAction() {
    }

    public void init(WebDriver driver, Member member, Date minDate) {
        this.driver = driver;
        this.member = member;
        this.minDate = minDate;
    }

    public void run(ActionCaller caller) {
        if (running) {
            return;
        }
        running = true;
        Task task = new Task();
        try {
            task.setMemberId(member.getId());
            task.setName("自动处理评论任务");
            task.setStartTime(new Date());
            task.setStatus(TaskStatus.ING);
            task.setSummary(String.format(summaryFormat, 0, 0));
            taskService.save(task);
            init();
            int page = 1;
            this.driver.get(uri);
            ThreadUtil.sleep(1000);
            List<WebElement> elements;
            int total = 0, delNum = 0;
            do {
                elements = getWebElements();
                if (CollectionUtil.isNotEmpty(elements)) {
                    total += elements.size();
                    task.setSummary(String.format(summaryFormat, total, delNum));
                    taskService.save(task);
                    caller.call();
                    delNum = collectData(elements) + delNum;
                    task.setSummary(String.format(summaryFormat, elements.size(), delNum));
                    taskService.save(task);
                    caller.call();
                    WebElement next = this.driver.findElement(By.cssSelector(".W_pages .next"));
                    if (!next.getAttribute("class").contains("page_dis")) {
                        next.click();
                        waitPage(++page);
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            } while (true);
            this.member.setLastCommentId(this.curMaxCommentId);
            this.memberService.save(this.member);
        } catch (Exception e) {
            logger.warn("", e);
        } finally {
            task.setStatus(TaskStatus.FINISHED);
            task.setEndTime(new Date());
            taskService.save(task);
            caller.call();
            running = false;
        }
    }

    private int collectData(List<WebElement> elements) {
        int delNum = 0;
        for (WebElement element : elements) {
            try {
                long commentId = Long.parseLong(element.getAttribute("comment_id"));
                if (this.curMaxCommentId == 0) {
                    this.curMaxCommentId = commentId;
                }
                if (commentId <= this.lastCommentId || commentId > this.curMaxCommentId) {
                    continue;
                }
                String href = element.findElement(By.cssSelector(".face a")).getAttribute("href");
                String nick = element.findElement(By.cssSelector(".WB_detail .WB_info a")).getText();
                String text = element.findElement(By.cssSelector(".WB_detail .WB_text")).getText();
                String timeStr = element.findElement(By.cssSelector(".WB_detail .WB_from")).getText();
                Matcher matcher = timePattern.matcher(timeStr);
                if (matcher.find()) {
                    Date date = DateUtils.parseDate(matcher.group(), new String[]{"MM月dd日 HH:mm", "yyyy年MM月dd日 HH:mm", "M月dd日 HH:mm"});
                    if (date.before(minDate)) {
                        break;
                    }
                }
                WebElement imgEl = element.findElement(By.cssSelector(".face a img"));
                String img = imgEl.getAttribute("src");
                String usercard = imgEl.getAttribute("usercard").replace("id=", "");
                Long myFansId = Long.valueOf(usercard);
                MyFans fans = fansMap.get(myFansId);
                if (fans == null) {
                    fans = new MyFans();
                    fans.setId(myFansId);
                    fans.setType(WordType.MASS);
                    fansMap.put(myFansId, fans);
                }
                fans.setNick(nick);
                fans.setHome(href);
                fans.setHeadImg(img);
                fans.setMemberId(this.member.getId());
                if (fans.getType().equals(WordType.BLACK)) {
                    deleteComment(element, this.member.getConfig().isOpenBlack());
                } else {
                    for (Word word : words) {
                        if (text.contains(word.getWord())) {
                            word.setHitNum(word.getHitNum() + 1);
                            this.hitWords.add(word);
                            fans.setType(word.getType());
                            if (word.getType() == WordType.BLACK) {
                                fans.setBadNum(fans.getBadNum() + 1);
                                deleteComment(element, member.getConfig().isOpenBlack() && member.getConfig().getBadNumLimit() <= fans.getBadNum());
                                delNum += 1;
                                break;
                            } else if (word.getType() == WordType.IRON) {
                                fans.setGoodNum(fans.getGoodNum() + 1);
                                break;
                            }
                        }
                    }
                }
            } catch (Throwable ignore) {
                logger.warn("comment err: ", ignore);
            }
        }
        if (!fansMap.isEmpty()) {
            fansService.save(fansMap.values());
            if (!hitWords.isEmpty()) {
                wordService.saves(hitWords);
            }
        }
        return delNum;
    }

    private List<WebElement> getWebElements() {
        do {
            try {
                return driver.findElements(By.cssSelector(".WB_feed_comment .WB_cardwrap[node-type=feed_commentList_comment]"));
            } catch (Throwable e) {
                ThreadUtil.sleep(600);
            }
        } while (true);
    }

    private void waitPage(int page) {
        do {
            try {
                WebElement pageEl = this.driver.findElement(By.cssSelector(".W_pages .page.S_bg1"));
                if (Integer.parseInt(pageEl.getText()) == page) {
                    break;
                }
                ThreadUtil.sleep(500);
            } catch (Throwable e) {
                ThreadUtil.sleepSeconds(500);
            }
        } while (true);
    }

    private void deleteComment(WebElement element, boolean addList) {
        ActionUtil.click(element, ".screen_box");
        ActionUtil.click(element, ".layer_menu_list a[action-type=delComment]");
        ThreadUtil.sleepSeconds(1);
        if (addList) {
            ActionUtil.click(driver, ".W_layer input[name=block_user]");
        }
        ActionUtil.click(driver, ".W_layer .W_layer_btn a[action-type=ok]");
        ThreadUtil.sleepSeconds(1);
    }

    private void init() {
        logger.info("comment action init start");
        this.words = wordService.listMyWords(this.member.getId());
        this.lastCommentId = this.memberService.get(this.member.getId()).getLastCommentId();
        this.curMaxCommentId = 0;
        this.fansMap.clear();
    }

}
