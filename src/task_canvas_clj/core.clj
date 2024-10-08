(ns task-canvas-clj.core
  (:gen-class)
  (:require
   [task-canvas-clj.port.task-canvas :as task-canvas-port]
   [task-canvas-clj.driver.task-canvas :as task-canvas-driver]
   [task-canvas-clj.use-case.todo :as task-canvas-todo-usecase]
   [task-canvas-clj.use-case.sign-in :as task-canvas-signin-usecase]
   [task-canvas-clj.use-case.system :as task-canvas-system]))

(def task-canvas-driver
  (reify task-canvas-port/TaskCanvasApiPort
    (post-todo [_ todo authorization] (task-canvas-driver/post-todo todo authorization))
    (get-todos [_ authorization] (task-canvas-driver/get-todos authorization))
    (delete-todo [_ todo-id authorization] (task-canvas-driver/delete-todo todo-id authorization))
    (sign-in [_ email password] (task-canvas-driver/sign-in email password))))

(defn -main
  "todo-canvas-api command line interface"
  [& args]
  (let [command (first args)
        authorization (task-canvas-signin-usecase/sign-in {:task-canvas-driver task-canvas-driver} "test@test.com" "123456789")]
    (case command
      "ping" (task-canvas-clj.use-case.system/system-ping)
      "get-todos" (println (task-canvas-todo-usecase/get-todos {:task-canvas-driver task-canvas-driver} authorization))
      "create-todos" (task-canvas-todo-usecase/store-todo {:task-canvas-driver task-canvas-driver} authorization)
      "delete-all-todos" (task-canvas-todo-usecase/delete-all {:task-canvas-driver task-canvas-driver} authorization)
      (println "Invalid command"))))