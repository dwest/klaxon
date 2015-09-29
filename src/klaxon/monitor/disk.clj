(ns klaxon.monitor.disk
  (:require [clojure.string :as string]
            [klaxon.monitor.util :as util])
  (:use [clojure.java.shell]))

(defn df []
  (-> (sh "df")
      :out
      string/split-lines
      util/lines->cols
      util/table->map))

(defn du [dir & [summary]]
  (let [args (remove nil? ["du" (if summary "-s" "-c") "--block-size" "1" dir])
        entries
        (-> (apply sh args)
            :out
            string/split-lines
            util/lines->cols
            ((partial util/table->map [:bytes :file])))]
    (if summary
      (first entries)
      entries)))
      
