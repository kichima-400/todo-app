package com.example.todobackend.controller;

import com.example.todobackend.entity.Todo;
import com.example.todobackend.repository.TodoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoRepository todoRepository;
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // 一覧取得
    @GetMapping
    public List<Todo> getAllTodos() {
        logger.info("getAllTodos() called 一覧取得"); // デバッグ用のログ出力
        return todoRepository.findAll();
    }

    // 新規追加
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        logger.info("createTodo() called 新規追加"); // デバッグ用のログ出力
        return todoRepository.save(todo);
    }

    // IDを指定してToDoを更新
    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
        logger.info("updateTodo : ID={} updatedTodo={} called 更新", id, updatedTodo.isCompleted()); // デバッグ用のログ出力
        return todoRepository.findById(id)
                .map(todo -> {
                    todo.setTitle(updatedTodo.getTitle());
                    todo.setCompleted(updatedTodo.isCompleted());
                    return todoRepository.save(todo);
                })
                .orElseThrow(() -> new RuntimeException("Todo not found"));
    }

    // IDを指定してToDoを削除
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        logger.info("deleteTodo : ID={} called 削除", id);
        todoRepository.deleteById(id);
    }

}