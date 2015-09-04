package com.monview



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class KpiController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Kpi.list(params), model:[kpiInstanceCount: Kpi.count()]
    }

    def show(Kpi kpiInstance) {
        respond kpiInstance
    }

    def create() {
        respond new Kpi(params)
    }

    @Transactional
    def save(Kpi kpiInstance) {
        if (kpiInstance == null) {
            notFound()
            return
        }

        if (kpiInstance.hasErrors()) {
            respond kpiInstance.errors, view:'create'
            return
        }

        kpiInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'kpi.label', default: 'Kpi'), kpiInstance.id])
                redirect kpiInstance
            }
            '*' { respond kpiInstance, [status: CREATED] }
        }
    }

    def edit(Kpi kpiInstance) {
        respond kpiInstance
    }

    @Transactional
    def update(Kpi kpiInstance) {
        if (kpiInstance == null) {
            notFound()
            return
        }

        if (kpiInstance.hasErrors()) {
            respond kpiInstance.errors, view:'edit'
            return
        }

        kpiInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Kpi.label', default: 'Kpi'), kpiInstance.id])
                redirect kpiInstance
            }
            '*'{ respond kpiInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Kpi kpiInstance) {

        if (kpiInstance == null) {
            notFound()
            return
        }

        kpiInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Kpi.label', default: 'Kpi'), kpiInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'kpi.label', default: 'Kpi'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
