(ns task-canvas-clj.use-case.todo
  (:require [faker.lorem :as faker-lorem]
            [task-canvas-clj.port.task-canvas :as task-canvas-port]))

(defn- generate-fake-content
  [n] (take n (faker-lorem/paragraphs)))

(defn- generate-fake-completed
  [] (rand-nth [true false]))

(defn- generate-faker-todos
  [n] (repeatedly n (fn [] {:content (first (generate-fake-content 1))
                            :completed (generate-fake-completed)})))

(defn store-todo
  [{:keys [task-canvas-driver]}]
  (let [todos (generate-faker-todos 20)]
    (doseq [todo todos]
      (task-canvas-port/post-todo task-canvas-driver todo))
    nil))

(defn get-todos
  [{:keys [task-canvas-driver]}]
  (task-canvas-port/get-todos task-canvas-driver))