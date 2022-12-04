(ns com.tylerkindy.adventofcode.generate
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

(defn pad-day [day]
  (cond
    (<= day 0) (throw (RuntimeException. "Invalid day"))
    (< day 10) (str "0" day)
    :else (str day)))

(defn find-templates []
  (->> (io/file "template")
       file-seq
       (filter #(str/ends-with? (.getPath %1) "clj"))))

(defn build-path [file {:keys [day]}]
  (-> (.getPath file)
      (str/replace "template/" "")
      (str/replace "XX" day)))

(defn replace-binding [template [key value]]
  (str/replace template
               (str "TEMP_" (name key))
               value))

(defn replace-bindings [template bindings]
  (reduce replace-binding template bindings))

(defn do-render [file {:keys [day] :as bindings}]
  (-> file
      slurp
      (str/replace "XX" day)
      (replace-bindings bindings)))

(defn render-template [file bindings]
  [(build-path file bindings)
   (do-render file bindings)])

(defn render-templates [bindings]
  (->> (find-templates)
       (map #(render-template %1 bindings))
       (into {})))

(def required-args #{:day
                     :parsed-name
                     :part1-name
                     :example-answer})

(defn valid-args? [args]
  (set/subset? required-args
               (set (keys args))))

(defn generate [{:keys [day] :as args}]
  (when (not (valid-args? args))
    (throw (RuntimeException. (str "Required args: " required-args))))

  (let [day (pad-day day)
        bindings (assoc args :day day)
        files (render-templates bindings)]
    (doseq [[path file] files]
      (spit path file))))
