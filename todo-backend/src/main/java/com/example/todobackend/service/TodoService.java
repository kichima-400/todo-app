package com.example.todobackend.service;

import com.example.todobackend.controller.TodoController;
import com.example.todobackend.entity.Todo;
import com.example.todobackend.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    public List<Todo> getAllTodos() {
        logger.info("getAllTodos() called 一覧取得"); // デバッグ用のログ出力
        return todoRepository.findAll();
    }

    public Todo createTodo(Todo todo) {
        logger.info("createTodo() called 新規追加"); // デバッグ用のログ出力
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, Todo todoDetails) {
        logger.info("updateTodo : ID={} updatedTodo={} called 更新", id, todoDetails.isCompleted()); // デバッグ用のログ出力
        return todoRepository.findById(id).map(todo -> {
            todo.setTitle(todoDetails.getTitle());
            todo.setCompleted(todoDetails.isCompleted());
            return todoRepository.save(todo);
        }).orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    }

    public void deleteTodo(Long id) {
        logger.info("deleteTodo : ID={} called 削除", id);
        todoRepository.deleteById(id);
    }

    public Todo toggleCompleted(Long id) {
        logger.info("toggleCompleted : ID={} called ToDo完了操作", id);
        return todoRepository.findById(id).map(todo -> {
            todo.setCompleted(!todo.isCompleted());
            return todoRepository.save(todo);
        }).orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    }
}
