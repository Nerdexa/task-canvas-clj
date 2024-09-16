(ns task-canvas-clj.driver.task-canvas
  (:require [clj-http.client :as client]))

(defn post-todo
  [todo] (let [path "http://localhost:8080/v1/todos"]
           (try (client/post path {:content-type :json
                                   :form-params todo})
                (catch Exception e
                  (throw (ex-info
                          (str "Failed to post todo: " todo)
                          {:cause e}))))))
