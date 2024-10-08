(ns task-canvas-clj.port.task-canvas)

(defprotocol TaskCanvasApiPort
  (post-todo [this todo authorization])
  (get-todos [this authorization])
  (delete-todo [this todo-id authorization])
  (sign-in [this email password]))