(ns task-canvas-clj.use-case.sign-in
  (:require [task-canvas-clj.port.task-canvas :as task-canvas-port]))

(defn sign-in
  [{:keys [task-canvas-driver]} email password]
  (try
    (let [res-sign-in (task-canvas-port/sign-in task-canvas-driver email password)
          authorization (get-in res-sign-in [:headers "Authorization"])]
      (if (nil? authorization)
        (do
          (println "Failed to sign in, error: No authorization header found.")
          nil)
        (do
          (println "Successfully signed in.")
          authorization)))
    (catch Exception e
      (println (str "Failed to sign in, error: " (.getMessage e)))
      nil)))