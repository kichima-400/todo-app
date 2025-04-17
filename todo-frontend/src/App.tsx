import { useEffect, useState } from 'react'
import { Todo } from './types/Todo'

function App() {
  const [todos, setTodos] = useState<Todo[]>([])
  const [newTitle, setNewTitle] = useState('')
  const [editingId, setEditingId] = useState<number | null>(null)
  const [editingTitle, setEditingTitle] = useState<string>('')

  useEffect(() => {
    fetchTodos()
  }, [])

  const fetchTodos = () => {
    fetch('http://localhost:8080/api/todos')
      .then(res => res.json())
      .then(data => setTodos(data))
  }

  const handleAddTodo = () => {
    if (!newTitle.trim()) return

    fetch('http://localhost:8080/api/todos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: newTitle, completed: false }),
    })
      .then(res => res.json())
      .then(createdTodo => {
        setTodos([...todos, createdTodo])
        setNewTitle('') // 入力クリア
      })
  }

  const handleDeleteTodo = (id: number) => {
    fetch(`http://localhost:8080/api/todos/${id}`, {
      method: 'DELETE',
    }).then(() => {
      setTodos(todos.filter(todo => todo.id !== id))
    })
  }

  const handleToggleCompleted = (todo: Todo) => {
    const updatedTodo = { ...todo, completed: !todo.completed }

    fetch(`http://localhost:8080/api/todos/${todo.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(updatedTodo),
    })
      .then(res => res.json())
      .then(returnedTodo => {
        setTodos(todos.map(t => (t.id === todo.id ? returnedTodo : t)))
      })
  }

  const handleSaveEdit = (todo: Todo) => {
    const updatedTodo = { ...todo, title: editingTitle }

    fetch(`http://localhost:8080/api/todos/${todo.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(updatedTodo),
    })
      .then(res => res.json())
      .then(returned => {
        setTodos(todos.map(t => (t.id === todo.id ? returned : t)))
        setEditingId(null)
        setEditingTitle('')
      })
  }

  return (
    <div style={{ padding: '1rem' }}>
      <h1>📋 memo list</h1>

      {/* 入力フォーム */}
      <div style={{ marginBottom: '1rem' }}>
        <input
          type="text"
          placeholder="新しいTodoを入力"
          value={newTitle}
          onChange={e => setNewTitle(e.target.value)}
        />
        <button onClick={handleAddTodo}>追加</button>
      </div>

      {/* 一覧表示 */}
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {todos.map(todo => (
          <li key={todo.id} className="todo-item">
            <input
              type="checkbox"
              checked={todo.completed}
              onChange={() => handleToggleCompleted(todo)}
              style={{ marginRight: '0.5rem' }}
            />

            {editingId === todo.id ? (
              <input
                type="text"
                value={editingTitle}
                onChange={e => setEditingTitle(e.target.value)}
                onBlur={() => handleSaveEdit(todo)}
                onKeyDown={e => {
                  if (e.key === 'Enter') handleSaveEdit(todo)
                }}
                autoFocus
              />
            ) : (
              <span
                onClick={() => {
                  setEditingId(todo.id)
                  setEditingTitle(todo.title)
                }}
                style={{
                  cursor: 'pointer',
                  textDecoration: todo.completed ? 'line-through' : 'none', // 完了したTodoは取り消し線
                  color: todo.completed ? 'gray' : 'black', // 完了したTodoはグレー
                  fontWeight: todo.completed ? 'normal' : 'bold', // 完了したTodoは通常の太さ
                }}
              >
                {todo.title}
              </span>
            )}

            <button onClick={() => handleDeleteTodo(todo.id)}>🗑️</button>
          </li>
        ))}
      </ul>
    </div>
  )
}

export default App

