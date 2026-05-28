package com.example.dartssimapi;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {

    // どのドメインからのアクセスでも一旦許可する（CORSエラー対策）
    @CrossOrigin(origins = "*") 
    @GetMapping("/api/hello")
    public Map<String, String> hello() {
        // JSON形式でデータを返す
        return Map.of("message", "Hello from Darts API! バックエンドとの通信成功です！");
    }
}