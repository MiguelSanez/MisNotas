package sanez.miguel.misnotas

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_agregar_nota.*
import java.io.File
import java.io.FileOutputStream

class AgregarNotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nota)
        btn_guardar.setOnClickListener {
            saveNotes()
        }

    }
    private fun saveNotes(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 235)
        }
        else{
            save()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            235 ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    save()
                }else{
                    Toast.makeText(this,"Error: Permisos denegados", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun save(){
        var titulo = et_titulo.text.toString()
        var contenido = et_contenido.text.toString()
        if(titulo == "" || contenido ==""){
            Toast.makeText(this,"Error: Campos vacios", Toast.LENGTH_SHORT).show()
        }else{
            try {
                val archive = File(location(), titulo+".txt")
                val fos = FileOutputStream(archive)
                fos.write(contenido.toByteArray())
                fos.close()
                Toast.makeText(this,"El archivo fue guardado en la carpeta publica", Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                Toast.makeText(this,"Error: el archivo no fue guardado", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }
    private fun location():String{
        val archivo = File(getExternalFilesDir(null),"notas")
        if (!archivo.exists()){
            archivo.mkdir()
        }
        return archivo.absolutePath
    }
}