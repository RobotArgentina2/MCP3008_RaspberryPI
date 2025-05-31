import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import java.io.IOException;

public class MCP3008Reader {

    public static void main(String[] args) throws IOException {
        // Crear dispositivo SPI en el canal 0 (CS0)
        SpiDevice spi = SpiFactory.getInstance(SpiChannel.CW0);

        int channel = 0; // Canal del MCP3008, de 0 a 7

        while (true) {
            int value = readADC(spi, channel);
            System.out.println("Valor del canal " + channel + ": " + value);
            try {
                Thread.sleep(1000); // Espera 1 segundo entre lecturas
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Función para leer el valor del MCP3008 en un canal específico
    private static int readADC(SpiDevice spi, int channel) throws IOException {
        // Construir el comando para MCP3008
        int command = 0b11000000 | (channel << 3);
        byte[] packet = new byte[3];
        packet[0] = (byte) 1;                 // Inicio de la transmisión
        packet[1] = (byte) (command);         // Comando para leer canal
        packet[2] = 0;                        // Byte de relleno

        byte[] result = spi.write(packet);

        // La respuesta contiene los 10 bits de datos
        int value = ((result[1] & 0x03) << 8) | (result[2] & 0xff);

        return value;
    }
}