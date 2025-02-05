//package com.brianxiadong.spring_ai_demo.chapter03.controller;
//
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.image.ImageModel;
//import org.springframework.ai.image.ImagePrompt;
//import org.springframework.ai.image.ImageResponse;
//import org.springframework.ai.qianfan.QianFanImageOptions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Flux;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/chapter03")
//public class ImageController {
//
//    private final ImageModel imageModel;
//
//    @Autowired
//    public ImageController(ImageModel imageModel) {
//        this.imageModel = imageModel;
//    }
//
//    @GetMapping("/image")
//    public Map<String, Object> generate(@RequestParam(value = "message", defaultValue = "A light cream colored mini golden doodle") String message) {
//        ImageResponse response = imageModel.call(
//                new ImagePrompt(message,
//                        QianFanImageOptions.builder()
//                                .N(4)
//                                .height(1024)
//                                .width(1024).build())
//        );
//
//        // 从响应中提取图片URL列表
//        return Map.of(
//            "message", "图片生成成功",
//            "images", response.getResults().stream()
//                          .map(result -> result.getOutput().getB64Json())
//                          .toList()
//        );
//    }
//
//
//}
