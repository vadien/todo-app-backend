package io.nology.todos.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.nology.todos.common.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody CreateTodoDTO data) throws Exception {
        Todo createdTodo = this.todoService.createTodo(data);
        return new ResponseEntity<Todo>(createdTodo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Todo>> findAllTodos() {
        List<Todo> todoList = this.todoService.findAllNotArchived();
        return new ResponseEntity<>(todoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> findById(@PathVariable long id) throws Exception {
        Optional<Todo> result = this.todoService.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Could not find todo with id" + id);
        }
        Todo foundTodo = result.get();
        return new ResponseEntity<>(foundTodo, HttpStatus.OK);
    }

    // TODO: Get by category search

    @PatchMapping("/{id}")
    public ResponseEntity<Todo> updateTodoById(@PathVariable Long id, @Valid @RequestBody UpdateTodoDTO data)
            throws Exception {
        Optional<Todo> result = this.todoService.updateTodoById(id, data);
        Todo updatedTodo = result.orElseThrow(() -> new NotFoundException("Could not find post for update"));
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Todo> completeTodoById(@PathVariable Long id, @Valid @RequestBody UpdateTodoDTO data)
            throws Exception {
        Optional<Todo> result = this.todoService.completeTodoById(id);
        Todo completedTodo = result.orElseThrow(() -> new NotFoundException("Could not find post for completion"));
        return new ResponseEntity<>(completedTodo, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Todo> deleteTodoById(@PathVariable Long id) throws Exception {
        Optional<Todo> result = this.todoService.deleteTodoById(id);
        Todo deletedTodo = result.orElseThrow(() -> new NotFoundException("Could not find post for deletion"));
        return new ResponseEntity<>(deletedTodo, HttpStatus.OK);
    }
}
