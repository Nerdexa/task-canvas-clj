(ns task-canvas-clj.driver.task-canvas
  (:require [cheshire.core :as json]
            [clj-http.client :as client]))

(defn- todo-url [^String path]
  (let [base "http://localhost:8080/v1/todos"]
    (if (empty? path)
      base
      (str base "/" path))))

(defn- with-auth [request authorization]
  (assoc-in request [:headers "Authorization"] authorization))

(defn post-todo [todo token]
  (let [path (todo-url "")]
    (try
      (client/post path (with-auth {:headers {"Content-Type" "application/json"}
                                    :body (json/generate-string todo)} token))
      (catch Exception e
        (throw (ex-info
                (str "Failed to post todo: " todo)
                {:cause e}))))))

(defn get-todos [token]
  (let [path (todo-url "")]
    (try
      (let [response (client/get path (with-auth {:headers {"Content-Type" "application/json"}} token))]
        (json/parse-string (:body response) true))
      (catch Exception e
        (println "Error during get-todos:" (.getMessage e))
        (throw (ex-info
                "Failed to get todos"
                {:cause e}))))))

(defn delete-todo [todo-id token]
  (let [path (todo-url todo-id)]
    (try
      (let [response (client/delete path (with-auth {:headers {"Content-Type" "application/json"}} token))]
        (get-in response [:body]))
      (catch Exception e
        (println "Error during delete-todo:" (.getMessage e))
        (throw (ex-info
                (str "Failed to delete todo: " todo-id)
                {:cause e}))))))

(defn sign-in [email password]
  (let [path "http://localhost:8080/v1/signIn"]
    (try
      (let [response (client/post path {:headers {"Content-Type" "application/json"}
                                        :body (json/generate-string {:email email
                                                                     :password password})})]
        response)
      (catch Exception e
        (println "Error during sign-in:" (.getMessage e))
        (throw (ex-info
                "Failed to sign in"
                {:cause e}))))))