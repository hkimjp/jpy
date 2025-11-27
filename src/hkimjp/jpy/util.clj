(ns hkimjp.jpy.util
  (:require
   ; [environ.core :refer [env]]
   ; [java-time.api :as jt]
   [hkimjp.datascript :as ds]))

; pseudo class

(def btn "mx-1 px-1 text-white bg-sky-500 hover:bg-sky-700 active:bg-red-500 rounded")

(def input-box "px-1 border-1 rounded")

; queries

(defn current-problem []
  (ds/qq '[:find [?e ?id ?problem]
           :keys e   id   problem
           :where
           [?e :current ?id]
           [?id :problem ?problem]]))

(defn list-answers [author]
  (ds/qq '[:find ?e ?num ?datetime
           :in $ ?author
           :where
           [?e :login ?author]
           [?e :p/id ?num]
           [?e :datetime ?datetime]]
         author))

(defn problems-all []
  (ds/qq '[:find ?e ?valid ?problem
           :keys e  valid  problem
           :where
           [?e :valid ?valid]
           [?e :problem ?problem]]))

(defn user [request]
  (get-in request [:session :identity]))
