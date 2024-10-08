(ns task-canvas-clj.use-case.todo
  (:require [faker.lorem :as faker-lorem]
            [cheshire.core :as json]
            [task-canvas-clj.port.task-canvas :as task-canvas-port]))

(defn- generate-fake-content
  [n] (take n (faker-lorem/paragraphs)))

(defn- generate-fake-completed
  [] (rand-nth [true false]))

(defn- generate-faker-todos
  [n] (repeatedly n (fn [] {:content (first (generate-fake-content 1))
                            :completed (generate-fake-completed)})))

(defn store-todo
  [{:keys [task-canvas-driver]} authorization]
  (let [todos (generate-faker-todos 20)]
    (doseq [todo todos]
      (task-canvas-port/post-todo task-canvas-driver todo authorization))
    nil))

(defn get-todos
  [{:keys [task-canvas-driver]} authorization]
  (task-canvas-port/get-todos task-canvas-driver authorization))

(defn delete-all
  [{:keys [task-canvas-driver]} authorization]
  (try
    (let [res-todos (task-canvas-port/get-todos task-canvas-driver authorization)
          ->parse-json (if (string? res-todos)
                         (json/parse-string res-todos true)
                         res-todos)
          todos (:todos ->parse-json)]
      (if (seq todos)
        (doseq [id (map :id todos)]
          (try
            (task-canvas-port/delete-todo task-canvas-driver id authorization)
            (catch Exception e
              (println (str "Failed to delete todo with id: " id ", error: " (.getMessage e))))))
        (println "No todos found to delete.")))
    (catch Exception e
      (println (str "Failed to retrieve todos, error: " (.getMessage e))))))