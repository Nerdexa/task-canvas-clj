(ns task-canvas-clj.port.task-canvas)

(defprotocol TaskCanvasApiPort
  (post-todo [this todo])
  (get-todos [this])
  (delete-all-todos [this]))