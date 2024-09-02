(ns task-canvas-clj.core
  (:gen-class)
  (:require [faker.lorem :as faker-lorem]
            [faker.company :as faker-company]
            [clj-http.client :as client]))

(defn- system-ping
  [] (println "pong"))

(defn- generate-fake-content
  [n] (take n (faker-lorem/paragraphs)))

(defn- generate-fake-title
  [n] (take n (faker-company/names)))

(defn- generate-fake-completed
  [] (rand-nth [true false]))

(defn- generate-faker-todos
  [n] (repeatedly n (fn [] {:title (first (generate-fake-title 1))
                            :content (first (generate-fake-content 1))
                            :completed (generate-fake-completed)})))

(defn- post-todo
  [todo] (let [path "http://localhost:8080/todos"]
           (try (client/post path {:content-type :json
                                   :body todo})
                (catch Exception e
                  (throw (ex-info
                          (str "Failed to post todo: " todo)
                          {:cause e}))))))

(defn- create-todos
  [n] (let [todos (generate-faker-todos n)]
        (doseq [todo todos]
          (post-todo todo))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [command (first args)]
    (case command
      "ping" (system-ping)
      "create-todos" (create-todos 20))))
