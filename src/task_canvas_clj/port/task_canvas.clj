(ns task-canvas-clj.port.task-canvas)

(defprotocol TaskCanvasApiPort
  (post-todo [this todo])
  (delete-all-todos [this]))