(ns task-canvas-clj.core
  (:gen-class)
  (:require
   [task-canvas-clj.port.task-canvas :as task-canvas-port]
   [task-canvas-clj.driver.task-canvas :as task-canvas-driver]
   [task-canvas-clj.use-case.todo :as task-canvas-usecase]
   [task-canvas-clj.use-case.system :as task-canvas-system]))

(def task-canvas-driver
  (reify task-canvas-port/TaskCanvasApiPort
    (post-todo [_ todo] (task-canvas-driver/post-todo todo))
    (get-todos [_] (task-canvas-driver/get-todos))
    (delete-todo [_ todo-id] (task-canvas-driver/delete-todo todo-id))))

(defn -main
  "todo-canvas-api command line interface"
  [& args]
  (let [command (first args)]
    (case command
      "ping" (task-canvas-clj.use-case.system/system-ping)
      "get-todos" (println (task-canvas-usecase/get-todos {:task-canvas-driver task-canvas-driver}))
      "create-todos" (task-canvas-usecase/store-todo {:task-canvas-driver task-canvas-driver})
      "delete-all-todos" (task-canvas-usecase/delete-all {:task-canvas-driver task-canvas-driver})
      (println "Invalid command"))))
