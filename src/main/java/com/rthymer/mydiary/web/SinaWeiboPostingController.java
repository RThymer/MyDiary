package com.rthymer.mydiary.web;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.rthymer.mydiary.entity.DiaryEntry;
import com.rthymer.mydiary.service.DiaryService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import weibo4j.Timeline;
import weibo4j.http.ImageItem;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.List;


@Controller
@ResponseBody
@RequestMapping(value = "/postweibo")
public class SinaWeiboPostingController {

    @Autowired
    private DiaryService diaryService;

    @RequestMapping(value = "/api", method = RequestMethod.POST)
    private String postWeiboByDiaryId(HttpServletRequest request) {
        Integer diaryId = Integer.parseInt(request.getParameter("diary_id"));
        DiaryEntry diary = diaryService.getDiaryById(diaryId);
        String content = diary.getTextContent();
        String imgPath = diary.getImgPath();
        String[] imgPathArray = imgPath.split("/");
        boolean status = new SinaWeiboApiPosting().postWeibo(content, imgPathArray[imgPathArray.length - 1], diaryId);
        return status ? "Success" : "Fail";
    }

}


class SinaWeiboApiPosting {
    public boolean postWeibo(String content, String pic, Integer id) {
        String accessToken = "2.00irUXAHDwQSuC5baa38975eRvhdvB";
        String contentWeibo = content + " https://www.baidu.com/";
        String picPath = "/home/rthymer/Documents/DiaryPics/" + pic;

        //含点的文件名会导致异常，待解决
        //已解决：输入流读进来再保存到输出流，不用库函数的复制等功能
        File sourcefile = new File(picPath);
        File tempFile = new File("/home/rthymer/Documents/DiaryPics/diaryId_" + id + ".jpg");

        try {
            InputStream inputStream = new FileInputStream(sourcefile);
            OutputStream outputStream = new FileOutputStream(tempFile);
            int byteCount = 0;
            byte[] bytes = new byte[1024];
            while ((byteCount = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, byteCount);
                outputStream.flush();
            }
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(tempFile);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            Timeline timeline = new Timeline(accessToken);
            try {
                Status status = timeline.share(contentWeibo, new ImageItem(bytes));
                //Log.logInfo(status.toString());
            } catch (WeiboException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}

class SinaWeiboRawPosting {
    //跳不过手势验证。。。新浪好狠
    public boolean postWeibo() throws IOException, InterruptedException {
        String baseUrl = "https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=http%3A%2F%2Fm.weibo.cn%2F";
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (iPad; CPU OS 7_0_2 like Mac OS X) " +
                "AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53");
        HtmlPage page = webClient.getPage(baseUrl);
        Thread.sleep(1000);
        HtmlInput usr = (HtmlInput) page.getElementById("loginName");
        usr.setValueAttribute("18627795737");
        HtmlInput pwd = (HtmlInput) page.getElementById("loginPassword");
        pwd.setValueAttribute("fuweijie110");
        DomElement button = page.getElementById("loginAction");
        page = (HtmlPage) button.click();
        Thread.sleep(1000);
        DomNodeList<DomElement> button2 = page.getElementsByTagName("a");
        page = (HtmlPage) button2.get(4).click();
        Thread.sleep(1000);
        DomNodeList<DomElement> button3 = page.getElementsByTagName("a");
        HtmlTextArea content = (HtmlTextArea) page.getElementById("txt-publisher");
        DomElement fasong = button3.get(1);
        content.focus();
        content.setText("暴力发微博测试。\n" + new Date());
        fasong.setAttribute("class", "fr txt-link");
        page = (HtmlPage) fasong.click();
        Thread.sleep(5000);
        System.out.println(page.asText());
        return true;
    }

}