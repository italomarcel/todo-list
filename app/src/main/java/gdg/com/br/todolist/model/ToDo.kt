package gdg.com.br.todolist.model

class ToDo {
  val task: String

  constructor() {}
  constructor(task: String) {
    this.task = task
  }
}