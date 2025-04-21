
package com.example.todobackend.controller;

import com.example.todobackend.entity.Todo;
import com.example.todobackend.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Test
    public void testGetTodos_ReturnsEmptyList() throws Exception {
        when(todoService.getAllTodos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/todos").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testGetTodos_ReturnsMultipleItems() throws Exception {
        List<Todo> mockTodoList = Arrays.asList(
            new Todo(1L, "Test 1", false),
            new Todo(2L, "Test 2", true)
        );

        when(todoService.getAllTodos()).thenReturn(mockTodoList);

        mockMvc.perform(get("/api/todos").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [
                        {"id":1,"title":"Test 1","completed":false},
                        {"id":2,"title":"Test 2","completed":true}
                    ]
                """));
    }

    @Test
    public void testDeleteTodo_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testToggleCompleted_ReturnsUpdatedTodo() throws Exception {
        Todo toggledTodo = new Todo(1L, "Test Todo", true);

        when(todoService.toggleCompleted(1L)).thenReturn(toggledTodo);

        mockMvc.perform(put("/api/todos/1/toggle")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {"id":1,"title":"Test Todo","completed":true}
                """));
    }
}
