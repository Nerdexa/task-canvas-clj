(ns task-canvas-clj.driver.task-canvas
  (:require [clj-http.client :as client]))

(defn- url [^String path]
  (let [base "http://localhost:8080/v1/todos"]
    (if (empty? path)
      base
      (str base "/" path))))

(defn post-todo
  [todo] (let [path (url "")]
           (try (client/post path {:content-type :json
                                   :form-params todo})
                (catch Exception e
                  (throw (ex-info
                          (str "Failed to post todo: " todo)
                          {:cause e}))))))

(defn get-todos
  [] (let [path (url "")]
       (println "Requesting todos from URL:" path)
       (try
         (let [response (client/get path)]
           (get-in response [:body]))
         (catch Exception e
           (println "Error during get-todos:" (.getMessage e))
           (throw (ex-info
                   "Failed to get todos"
                   {:cause e}))))))

(defn delete-todo [todo-id]
  (let [path (url todo-id)]
    (try
      (let [response (client/delete path)]
        (get-in response [:body]))
      (catch Exception e
        (println "Error during delete-todo:" (.getMessage e))
        (throw (ex-info
                (str "Failed to delete todo: " todo-id)
                {:cause e}))))))