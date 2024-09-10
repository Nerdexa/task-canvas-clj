(ns task-canvas-clj.use-case.todo-test
  (:require [clojure.test :as t]
            [mockfn.macros :as mockfn]
            [mockfn.matchers :as matcher]
            [task-canvas-clj.port.task-canvas :as task-canvas-port]
            [task-canvas-clj.use-case.todo :as sut]))

(t/deftest store-todo-test
  (t/testing "todoの登録"
    (let [deps {:task-canvas-driver :task-canvas-driver'}
          todo {:content "content" :completed false}]
      (with-redefs [sut/generate-faker-todos (fn [_] (repeat 20 todo))]
        (mockfn/verifying
         [(task-canvas-port/post-todo :task-canvas-driver' todo) nil (matcher/exactly 20)]
         (t/is (= nil (sut/store-todo deps))))))))