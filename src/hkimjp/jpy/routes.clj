(ns hkimjp.jpy.routes
  (:require
   [reitit.ring :as rr]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [taoensso.telemere :as t]
   [hkimjp.jpy.middleware :as m]
   [hkimjp.jpy.admin :as admin]
   ;[hkimjp.jpy.answers :as answers]
   [hkimjp.jpy.help :refer [help]]
   [hkimjp.jpy.login :refer [login login! logout!]]
   [hkimjp.jpy.scoreboard :as scoreboard]
   [hkimjp.jpy.workspace :as workspace]))

(defn routes []
  [["/" {:middleware []}
    ["" {:get login :post login!}]
    ["logout" logout!]
    ["help"   {:get help}]]
   ["/admin/" {:middleware [m/wrap-admin]}
    [""           {:get admin/admin}]
    ; ["new"        {:get admin/new  :post admin/upsert!}]
    ; ["update/:e"  {:get admin/edit :post admin/upsert!}]
    ; ["find"       {:post admin/eid}]
    ; ["delete"     {:post admin/delete!}]
    ]
   ["/workspace/" {:middleware [m/wrap-users]}
    ["" {:get workspace/index}]]
   ["/scoreboard/" {:middleware [m/wrap-users]}
    ["" {:get scoreboard/index}]]])

(defn root-handler
  [request]
  (t/log! :info (str (:request-method request) " - " (:uri request)))
  (let [handler
        (rr/ring-handler
         (rr/router (routes))
         (rr/routes
          (rr/create-resource-handler {:path "/"})
          (rr/create-default-handler
           {:not-found
            (constantly {:status 404
                         :headers {"Content-Type" "text/html"}
                         :body "<h1>ERROR</h1><p>not found</p>"})
            :method-not-allowed
            (constantly {:status 405
                         :body "not allowed"})
            :not-acceptable
            (constantly {:status 406
                         :body "not acceptable"})}))
         {:middleware [[wrap-defaults site-defaults]]})]
    (handler request)))

; (root-handler {:uri "/admin/eid" :request-method "post"})
