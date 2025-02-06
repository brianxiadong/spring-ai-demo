package com.brianxiadong.spring_ai_demo.chapter06.controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/chapter06")
public class RAGController {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ChatModel chatModel;

    @PostMapping("/store")
    public ResponseEntity<Map<String, String>> store(@RequestBody Map<String, String> request) {
        String message = request.get("content");
        // Add the documents to Redis
        vectorStore.add(Collections.singletonList(new Document(message)));
        
        return ResponseEntity.ok(Map.of(
            "message", "数据保存成功"
        ));
    }

    // @PostMapping("/query")
    // public ResponseEntity<Map<String, String>> vectorQuery(@RequestParam(value = "message") String message){
    //     // Retrieve documents similar to a query
    //     List<Document> results = this.vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(1).build());

    //     if (results != null) {
    //         return ResponseEntity.ok(Map.of("message", "查询成功", "results",
    //                 Objects.requireNonNull(results.getFirst().getText())));
    //     }

    //     return ResponseEntity.badRequest().body(Map.of("message", "查询失败"));
    // }


    @PostMapping("/rag")
    public ResponseEntity<Map<String, String>> rag(@RequestBody Map<String, String> request) {
        String message = request.get("content");
        String content = ChatClient.builder(chatModel)
                .build().prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .user(message)
                .call()
                .content();

        return ResponseEntity.ok(Map.of(
            "message", "查询成功",
            "answer", Objects.requireNonNull(content)
        ));
    }
}
