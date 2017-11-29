import java.text.SimpleDateFormat
import java.util.Calendar
import groovy.time.*

// human-readable format
def dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

// convert time from UTC to Taiwan Time(+8:00)
def startTime = new Date((Calendar.getInstance()).getTimeInMillis() + (480 * 60000))
def strStartTime = dateFormat.format(startTime)

sleep 5

openshift.withCluster() {
    stage('Calculate Time Duration') {
        node {

            def endTime = new Date((Calendar.getInstance()).getTimeInMillis() + (480 * 60000))
            strEndTime = dateFormat.format(endTime)
            
            // calculate time duration
            TimeDuration duration = TimeCategory.minus(endTime, startTime)
            def strDuration = String.format("%02d", duration.getHours()) + ":" + String.format("%02d", duration.getMinutes()) + ":" + String.format("%02d", duration.getSeconds())
            // it's necessary to set it to null for avoiding "not serializable exception"
            duration = null

            echo "Start Time = ${strStartTime}"
            echo "End Time = ${strEndTime}"
            echo "Time Duration = ${strDuration}"
        } 
    } // ---------- End of stage('Configure IAAS')
}