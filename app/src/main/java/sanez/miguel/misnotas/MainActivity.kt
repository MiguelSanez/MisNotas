package sanez.miguel.misnotas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
    var notas = ArrayList<Nota>()
    lateinit var adaptador: AdaptadorNotas
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            var intent = Intent(this,AgregarNotaActivity::class.java)
            startActivityForResult(intent,123)
        }
        readNotes()
        adaptador = AdaptadorNotas(this,notas)
        listview.adapter = adaptador
    }
    private fun readNotes(){
        notas.clear()
        var archivo = File(location())

        if (archivo.exists()){
            var archivos = archivo.listFiles()
            if (archivos!= null){
                for (archivoAux in archivos){
                    readFile(archivo)
                }
            }
        }

    }
    private fun readFile(archive: File?) {
        val fis = FileInputStream(archive)
        val di = DataInputStream(fis)
        val br = BufferedReader(InputStreamReader(di))
        var strLine: String? = br.readLine()
        var myData =""

        while (strLine != null){
            myData += strLine
            strLine = br.readLine()
        }
        br.close()
        di.close()
        fis.close()
        var name = archive?.name?.substring(0,archive.name.length-4)
        var note = name?.let { Nota(it,myData) }
        note?.let { notas.add(it) }
    }
    private fun location():String{
        val folder = File(getExternalFilesDir(null),"notas")
        if (!folder.exists()){
            folder.mkdir()
        }
        return folder.absolutePath
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 123){
            readNotes()
            adaptador.notifyDataSetChanged()
        }
    }
}