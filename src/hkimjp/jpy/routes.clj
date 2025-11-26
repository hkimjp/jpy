(ns hkimjp.jpy.routes
  (:require
   [reitit.ring :as ring]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [hkimjp.jpy.admin :as admin]
   [hkimjp.jpy.answers :as answers]
   [hkimjp.jpy.event :as event]
   [hkimjp.jpy.help :refer [help]]
   [hkimjp.jpy.login :refer [login login! logout!]]
   [hkimjp.jpy.middleware :as m]
   [hkimjp.jpy.problems :as problems]
   [hkimjp.jpy.scoreboard :as scoreboard]
   [hkimjp.jpy.view :refer [error-page]]
   [hkimjp.jpy.workspace :as workspace]))

(defn under-construction
  [{{:keys [identity]} :session :as request}]
  (println "identity: " identity)
  (error-page [:div (str "under construction " (:uri request))]))

(def routes
  [["/"      {:get login :post login!}]
   ["/logout" logout!]
   ["/help"   {:get help}]
   ["/admin" {:middleware [m/wrap-admin]}
    [""           {:get admin/admin}]]
   ["/workspace" {:middleware [m/wrap-users]}
    ["" {:get workspace/index}]
    ["/answer/:e" {:get workspace/answer}]]
   ["/scoreboard" {:middleware [m/wrap-users]}
    ["" {:get scoreboard/index}]]
   ["/problems" {:middleware [m/wrap-admin]}
    ["/create"  {:post {:handler problems/create!}}]
    ["/current" {:post {:handler problems/current!}}]]
   ["/answers" {:middleware [m/wrap-users]}
    ["/upload" {:post answers/upload!}]]
   ["/event"
    ["" {:get {:handler event/event}}]
    ["/broadcat" {:get {:handler event/broadcast!}}]]
   ["/hx" under-construction]])

(def root-handler
  (ring/ring-handler
   (ring/router routes)
   (ring/routes
    (ring/create-resource-handler {:path "/"})
    (ring/create-default-handler
     {:not-found
      (constantly (error-page [:p "not found, check uri"]))
      :method-not-allowed
      (constantly (error-page [:p "not allowed"]))
      :not-acceptable
      (constantly (error-page [:p "not acceptable"]))}))
   {:middleware [[wrap-defaults site-defaults]]}))

; (root-handler {:request-method :get, :uri "/"})
; (root-handler {:request-method :get, :uri "/not"})
; (root-handler {:request-method :get, :uri "/favicon.ico"})


