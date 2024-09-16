(ns task-canvas-clj.core
  (:gen-class)
  (:require
   [clj-http.client :as client]
   [cheshire.core :as json]
   [task-canvas-clj.port.task-canvas :as task-canvas-port]
   [task-canvas-clj.driver.task-canvas :as task-canvas-driver]
   [task-canvas-clj.use-case.todo :as task-canvas-usecase]))

(defn- system-ping
  [] (println "pong"))

(defn- delete-todo
  [todo-id] (let [path (str "http://localhost:8080/v1/todos/" todo-id)]
              (try (client/delete path)
                   (catch Exception e
                     (println e)
                     (throw (ex-info
                             (str "Failed to delete todo: " todo-id)
                             {:cause e}))))))

(defn- get-todos
  [] (let [path "http://localhost:8080/v1/todos"]
       (-> (try (client/get path)
                (catch Exception e
                  (throw (ex-info
                          "Failed to get todos"
                          {:cause e}))))
           (get-in [:body]))))

(defn- get-todo-ids
  [] (let [todos-json (get-todos)
           todos-map (json/parse-string todos-json true)
           todos (:todos todos-map)]
       (vec (map :id todos))))

(defn- delete-all-todos
  [] (let [todo-ids (get-todo-ids)]
       (doseq [todo-id todo-ids]
         (delete-todo todo-id))
       (println (str "Deleted all todos"))))

(def task-canvas-driver
  (reify task-canvas-port/TaskCanvasApiPort
    (post-todo [_ todo] (task-canvas-driver/post-todo todo))))

(defn -main
  "todo-canvas-api command line interface"
  [& args]
  (let [command (first args)]
    (case command
      "ping" (system-ping)
      "get-todos" (println (get-todos))
      "create-todos" (task-canvas-usecase/store-todo {:task-canvas-driver task-canvas-driver})
      "delete-all-todos" (delete-all-todos)
      (println "Invalid command"))))
