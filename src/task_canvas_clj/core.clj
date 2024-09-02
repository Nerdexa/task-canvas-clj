(ns task-canvas-clj.core
  (:gen-class)
  (:require [faker.lorem :as faker-lorem]
            [clj-http.client :as client]
            [cheshire.core :as json]))

(defn- system-ping
  [] (println "pong"))

(defn- generate-fake-content
  [n] (take n (faker-lorem/paragraphs)))

(defn- generate-fake-completed
  [] (rand-nth [true false]))

(defn- generate-faker-todos
  [n] (repeatedly n (fn [] {:content (first (generate-fake-content 1))
                            :completed (generate-fake-completed)})))

(defn- post-todo
  [todo] (let [path "http://localhost:8080/v1/todos"]
           (try (client/post path {:content-type :json
                                   :form-params todo})
                (catch Exception e
                  (throw (ex-info
                          (str "Failed to post todo: " todo)
                          {:cause e}))))))

(defn- delete-todo
  [todo-id] (let [path (str "http://localhost:8080/v1/todos/" todo-id)]
              (println path)
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
       (println todo-ids)
       (doseq [todo-id todo-ids]
         (delete-todo todo-id))
       (println (str "Deleted all todos"))))

(defn- create-todos
  [n] (let [todos (generate-faker-todos n)]
        (doseq [todo todos]
          (post-todo todo))
        (println (str "Created todos"))))

(defn -main
  "todo-canvas-api command line interface"
  [& args]
  (let [command (first args)]
    (case command
      "ping" (system-ping)
      "get-todos" (println (get-todos))
      "create-todos" (create-todos 20)
      "delete-all-todos" (delete-all-todos))))
