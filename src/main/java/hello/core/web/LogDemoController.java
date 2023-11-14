package hello.core.web;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger; // ObjectProvider는 myLogger를 주입 받는게 아니라 DL이 가능한 provider가 주입되는 것

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        myLogger.setRequestUrl(requestUrl);

        System.out.println("myLogger = " + myLogger.getClass());

        myLogger.log("controller test");
        logDemoService.logic("testId");
        return "OK";
    }
}
