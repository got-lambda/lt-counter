(ns lt.plugins.counter
  (:require [lt.object :as object]
            [lt.objs.tabs :as tabs]
            [lt.objs.command :as cmd]
            [lt.objs.editor :as ed]
            [clojure.string :as string])
  (:require-macros [lt.macros :refer [defui behavior]]))

(defui hello-panel [this]
  [:h1 "Hello from counter!!!"])

(object/object* ::counter.hello
                :tags [:counter.hello]
                :name "counter"
                :init (fn [this]
                        (hello-panel this)))

(behavior ::on-close-destroy
          :triggers #{:close}
          :reaction (fn [this]
                      (when-let [ts (:lt.objs.tabs/tabset @this)]
                        (when (= (count (:objs @ts)) 1)
                          (tabs/rem-tabset ts)))
                      (object/raise this :destroy)))

(behavior ::count-lines
          :triggers #{:count-lines}
          :reaction (fn [this]
                      (js/alert (count (remove #{""} (string/split-lines (ed/->val this)))))))

(def hello (object/create ::counter.hello))

(cmd/command {:command ::count-lines
              :desc "counter: Count Lines"
              :exec (fn []
                      (when-let [ed (lt.objs.editor.pool/last-active)]
                        (object/raise ed :count-lines)))})
