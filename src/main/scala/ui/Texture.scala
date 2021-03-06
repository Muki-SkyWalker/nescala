package ui

import java.awt.image._
import java.nio.IntBuffer

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL12
import org.macrogl.Macrogl

object Texture {
  private val buffer = BufferUtils.createIntBuffer(256 * 240 * 4)

  def createTexture()(implicit gl:Macrogl):Int = {
    val texture = gl.createTexture()
    gl.bindTexture(Macrogl.TEXTURE_2D, texture)
    gl.texParameteri(Macrogl.TEXTURE_2D, Macrogl.TEXTURE_MIN_FILTER, Macrogl.NEAREST)
    gl.texParameteri(Macrogl.TEXTURE_2D, Macrogl.TEXTURE_MAG_FILTER, Macrogl.NEAREST)
    gl.texParameteri(Macrogl.TEXTURE_2D, Macrogl.TEXTURE_WRAP_S, Macrogl.CLAMP_TO_EDGE)
    gl.texParameteri(Macrogl.TEXTURE_2D, Macrogl.TEXTURE_WRAP_T, Macrogl.CLAMP_TO_EDGE)
    gl.bindTexture(Macrogl.TEXTURE_2D, 0)
    texture
  }

  def setTexture(im:BufferedImage)(implicit gl:Macrogl) = {

    val dataBuffer = IntBuffer.wrap(im.getRaster.getDataBuffer.asInstanceOf[DataBufferInt].getData)

    buffer.put(dataBuffer)
    buffer.flip()//FOR THE LOVE OF GOD DO NOT FORGET THIS
    // Use GL_UNSIGNED_INT_8_8_8_8_REV to reverse RGBA pixel format to BGRA on GPU
    gl.texImage2D(Macrogl.TEXTURE_2D, 0, Macrogl.RGBA, im.getWidth, im.getHeight, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer)
    buffer.rewind()
  }
}