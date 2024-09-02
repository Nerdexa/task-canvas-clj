(ns task-canvas-clj.core
  (:gen-class)
  (:require [faker.lorem :as faker-lorem]
            [faker.company :as faker-company]))

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

(defn- post-todos
  [todos] (let [path "http://localhost:8080/todos"]))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (println (generate-faker-todos 20)))
