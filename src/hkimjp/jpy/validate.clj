(ns hkimjp.jpy.validate
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [environ.core :refer [env]]
   [jx.java.shell :refer [timeout-sh]]
   [taoensso.telemere :as t]
   #_[hkimjp.datascript :as ds]))

(def ^:private timeout 10)

; (def ruff-path
;   (or (env :ruff-path) "/usr/local/bin/ruff"))

; (def python-path
;   (or (env :python-path) "/usr/bin/python3"))

; (def pytest-path
;   (or (env :pytest-path) "/usr/bin/pytest"))

(defn- create-tempfile-with
  "create a tempfile, save the contents of `answer` on it,
   returns the path of the created temp file."
  [answer]
  (let [f (fs/create-temp-file {:suffix ".py"})]
    (t/log! {:level :debug
             :id "create-tempfile-with"
             :data {:tempfile (str (fs/file f))}})
    (spit (fs/file f) answer)
    f))

(defn- ruff
  "ruff requires '\n' at the end of the code"
  [answer]
  (t/log! {:level :info :id "ruff"})
  (let [ruff-path (env :ruff-path)
        f (create-tempfile-with (str answer "\n"))
        ret (timeout-sh
             timeout
             ;; 0.13.*
             ;; (ruff-path "format" "--diff" (str (fs/file f)))
             ;; 0.14.7
             ruff-path "-q" "format" "--check" (str (fs/file f)))]
    (t/log! :debug (str "ruff result: " ret))
    (if-not (zero? (:exit ret))
      (fs/delete f)
      (throw (Exception. "using VScode/Ruff?")))))

(defn validate [author answer]
  (t/log! {:level :info :id "validate" :data {:answer answer}})
  (try
    (ruff answer)
    (catch Exception e
      (t/log! {:level :warn
               :msg   "validate error"
               :data  {:author author
                       :answer answer
                       :error (.getMessage e)}})
      (throw (Exception. (.getMessage e))))))
