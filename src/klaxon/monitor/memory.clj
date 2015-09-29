(ns klaxon.monitor.memory
  (:use [clojure.java.shell])
  (:require [clojure.string :as string]
            [klaxon.monitor.util :as util]))

(defn info []
  (-> (sh "cat" "/proc/meminfo")
      :out
      string/split-lines
      util/lines->cols
      util/pairs->map))

