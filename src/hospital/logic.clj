(ns hospital.logic)

(defn cabe-na-fila? [hospital departamento]
  (-> hospital
      (get departamento)
      count
      (< 5)))

(defn chega-em
  [hospital departamento pessoa]
    (if (cabe-na-fila? hospital departamento)
     (update hospital departamento conj pessoa)
     (throw (ex-info "Fila já está cheia" { :tentando-adicionar pessoa}))))

;funcao malvada que parece pura mas usa random e altera estado do random e loga
(defn chega-em-pausado-logando
  [hospital departamento pessoa]
  (println "Tentando adicionar uma pessoa " pessoa)
  (Thread/sleep (* (rand) 2000))
  (if (cabe-na-fila? hospital departamento)
    (do
      ;(Thread/sleep 2000)
        (println "Dando um update " pessoa)
        (update hospital departamento conj pessoa))
    (throw (ex-info "Fila já está cheia" { :tentando-adicionar pessoa}))))


(defn atende
  [hospital departamento]
  (update hospital departamento pop))

(defn proxima 
  "Retorna o próximo paciente da fila"
  [hospital departamento]
  (-> hospital
      departamento
      peek))

(defn transfere
  "Transfere o próximo paciente da fila de para a fila para"
  [hospital de para]
  (let [pessoa (proxima hospital de)]
    (-> hospital
    (atende de)
    (chega-em para pessoa))))

;; (atende-completo
;;  "somente para demonstrar que é possível retornar os dois (pop e peek)"
;;  [hospital departamento]
;;  {:paciente (update hospital departamento peek) 
;;   :hospital     (update hospital departamento pop)})


;; (atende-completo-que-chama-ambos
;;  "somente para demonstrar que é possível retornar os dois (pop e peek)"
;;  [hospital departamento]
;;  (let [fila (get hospital departamento)
;;        peek-pop (juxt peek pop)
;;        [pessoa fila-atualizada] (peek-pop fila)
;;        hospital-atualizado (update hospital assoc departamento fila-atualizada)]
;;    {:paciente pessoa
;;     :fila     fila-atualizada}))
 