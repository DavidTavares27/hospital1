(ns hospital.aula6
  (:use [clojure.pprint])
  (:require [hospital.model :as h.model]))

(defn cabe-na-fila? [fila]
  (-> fila
      count
      (< 5)))

(defn chega-em 
  [fila pessoa]
  (if (cabe-na-fila? fila)
  (conj fila pessoa)
  (throw (ex-info "Fila está cheia" {:tentando-adicionar pessoa}))))

(defn chega-em! 
  "troca de referencia via ref-set"
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (ref-set fila (chega-em @fila pessoa))))

(defn chega-em!
  "troca de referencia via alter"
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (alter fila chega-em pessoa)))

(defn simula-um-dia []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]
    
  (dosync  
   (chega-em! hospital "guilherme")
   (chega-em! hospital "maria")
   (chega-em! hospital "lucia")
   (chega-em! hospital "daniela")
   (chega-em! hospital "ana")
   ;(chega-em! hospital "paulo")
   )
    
  (pprint hospital)))

;(simula-um-dia)

(defn async-chega-em! [hospital pessoa]
  (future
    (Thread/sleep (rand 5000))
    (dosync
     (println "Tentando o codigo sicronizado" pessoa)
     (chega-em! hospital pessoa))))

(defn simula-um-dia-async []
  (let [hospital {:espera       (ref h.model/fila-vazia)
                  :laboratorio1 (ref h.model/fila-vazia)
                  :laboratorio2 (ref h.model/fila-vazia)
                  :laboratorio3 (ref h.model/fila-vazia)}]
    
    (def futures (mapv #(async-chega-em! hospital %) (range 10)))
    

    
     (future
       (dotimes [n 4]
       (Thread/sleep 2000)
       (pprint hospital)
       (pprint futures)))
    ))

(simula-um-dia-async)