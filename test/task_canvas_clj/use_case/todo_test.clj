(ns task-canvas-clj.use-case.todo-test
  (:require [clojure.test :as t]
            [task-canvas-clj.use-case.todo :as sut]
            [task-canvas-clj.port.task-canvas :as task-canvas-port]
            [mockfn.macros :as mockfn]
            [mockfn.matchers :as matcher]))

(t/deftest store-todo-test
  (t/testing "todoの登録"
    (let [deps {:task-canvas-driver :task-canvas-driver'}
          todo {:content "content" :completed false}]
      (mockfn/verifying [(task-canvas-port/post-todo :task-canvas-driver' todo) nil (matcher/exactly 1)]
                        (t/is (= nil (sut/store-todo deps)))))))