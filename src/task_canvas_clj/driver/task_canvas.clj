(ns task-canvas-clj.driver.task-canvas
  (:require [clj-http.client :as client])
  (:import [java.net URI]))

(defn- url [^String path]
  (let [base "http://localhost:8080/v1/todos"]
    (if (empty? path)
      base
      (str (.resolve ^URI (URI. base) path)))))

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
       (-> (try (client/get path)
                (catch Exception e
                  (throw (ex-info
                          "Failed to get todos"
                          {:cause e}))))
           (get-in [:body]))))