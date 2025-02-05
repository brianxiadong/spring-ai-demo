package com.brianxiadong.spring_ai_demo.chapter04.controller;

import org.springframework.ai.openai.audio.speech.SpeechModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chapter04")
public class TTSController {

    private final SpeechModel speechModel;

    @Autowired
    public TTSController(SpeechModel speechModel) {
        this.speechModel = speechModel;
    }

    @GetMapping("/tts")
    public ResponseEntity<byte[]> generate(@RequestParam(value = "message", defaultValue = "你好，我是人工智能助手") String message) {
        // 调用语音合成服务
        byte[] audioData = speechModel.call(message);

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/mp3"));
        headers.setContentLength(audioData.length);
        
        // 返回音频数据
        return ResponseEntity.ok()
                .headers(headers)
                .body(audioData);
    }
}
