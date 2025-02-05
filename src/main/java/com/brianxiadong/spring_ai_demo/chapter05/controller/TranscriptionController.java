package com.brianxiadong.spring_ai_demo.chapter05.controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/chapter05")
public class TranscriptionController {

    private final OpenAiAudioTranscriptionModel transcriptionModel;

    @Autowired
    public TranscriptionController(OpenAiAudioTranscriptionModel transcriptionModel) {
        this.transcriptionModel = transcriptionModel;
    }

    @PostMapping("/transcription")
    public ResponseEntity<Map<String, String>> transcription(@RequestParam("file") MultipartFile file) {
        try {
            // 设置转录选项
            OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                    .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                    .temperature(0f)
                    .build();

            // 将 MultipartFile 转换为 Resource
            Resource audioResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            // 创建转录请求
            AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioResource, transcriptionOptions);

            // 调用转录服务
            AudioTranscriptionResponse response = this.transcriptionModel.call(transcriptionRequest);

            // 返回转录结果
            return ResponseEntity.ok(Map.of(
                "message", "语音识别成功",
                "text", response.getResult().getOutput()
            ));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "语音识别失败：" + e.getMessage()
            ));
        }
    }
}
