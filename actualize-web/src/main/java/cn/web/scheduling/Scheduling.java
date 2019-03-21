package cn.web.scheduling;

import cn.web.controller.CorpExportController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by Administrator on 2017/3/29.
 */
@Configuration
@EnableScheduling
public class Scheduling {

    private static Logger log = LoggerFactory.getLogger(CorpExportController.class);

    Integer i = 0;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void scheduler() {
        log.info("我来露个脸。。。。");
        log.info(">>>>>>>>> SchedulingConfig.scheduler()" + i ++ );
    }
    /*
    "0 0 12 * * ?"每天中午十二点触发

    "0 15 10 ? * *"每天早上10:15触发

    "0 15 10 * * ?"每天早上10:15触发

    "0 15 10 * * ? *"每天早上10:15触发

    "0 15 10 * * ? 2005"2005年的每天早上10:15触发

    "0 * 14 * * ?"每天从下午2点开始到2点59分每分钟一次触发

    "0 0/5 14 * * ?"每天从下午2点开始到2:55分结束每5分钟一次触发

    "0 0/5 14,18 * * ?"每天下午2点至2:55和6点至6点55分两个时间段内每5分钟一次触发

    "0 0-5 14 * * ?"每天14:00至14:05每分钟一次触发

    "0 10,44 14 ? 3 WED"三月的每周三的14:10和14:44触发

    "0 15 10 ? * MON-FRI"每个周一,周二,周三,周四,周五的10:15触发

    "0 15 10 15 * ?"每月15号的10:15触发

    "0 15 10 L * ?"每月的最后一天的10:15触发

    "0 15 10 ? * 6L"每月最后一个周五的10:15触发

    "0 15 10 ? * 6L 2002-2005"2002年至2005年的每月最后一个周五的10:15触发

    "0 15 10 ? * 6#3"每月的第三个周五的10:15触发

    "0 0 *//*1 * * ?"每小时执行一次

    "0 0/5 * * * ?"每五钟执行一次

    "0/5 * * * * ?"每五秒执行一次
    */
}
