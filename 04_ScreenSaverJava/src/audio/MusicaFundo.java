package audio;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * Música de fundo gerada em MIDI (não precisa de arquivo de áudio).
 * Usa o sintetizador embutido do Java e toca em loop contínuo.
 */
public class MusicaFundo {

    private static final int TICKS_POR_SEMINIMA = 4;
    private static final int COLCHEIA = TICKS_POR_SEMINIMA / 2;
    private static final float ANDAMENTO_BPM = 132;

    // Melodia: uma nota por colcheia (0 = pausa). 8 compassos: C - Am - F - G - C - Am - F - G
    private static final int[] MELODIA = {
        72, 76, 79, 76, 72, 76, 79, 84,
        81, 79, 76, 72, 74, 76, 72,  0,
        77, 81, 84, 81, 77, 81, 84, 81,
        79, 77, 76, 74, 72, 74, 76, 79,
        72, 76, 79, 76, 84, 79, 76, 72,
        69, 72, 76, 72, 81, 76, 72, 69,
        77, 76, 74, 72, 74, 76, 77, 79,
        79, 77, 76, 74, 71, 74, 79,  0
    };

    // Fundamental do acorde de cada compasso (baixo)
    private static final int[] BAIXO = { 48, 45, 41, 43, 48, 45, 41, 43 };

    private Sequencer sequencer;
    private boolean mudo = false;

    public void tocar() {
        try {
            if (sequencer == null) {
                sequencer = MidiSystem.getSequencer(); // já conectado ao sintetizador padrão
                sequencer.open();
                sequencer.setSequence(criarMusica());
                sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            }
            if (!mudo) sequencer.start();
        } catch (Exception e) {
            System.out.println("Não foi possível tocar a música de fundo: " + e.getMessage());
            sequencer = null;
        }
    }

    /** Liga/desliga a música (tecla M). */
    public void alternarMudo() {
        mudo = !mudo;
        if (sequencer == null) return;
        if (mudo) sequencer.stop();
        else sequencer.start();
    }

    public boolean isMudo() { return mudo; }

    public void parar() {
        if (sequencer != null) {
            sequencer.stop();
            sequencer.close();
            sequencer = null;
        }
    }

    private Sequence criarMusica() throws Exception {
        Sequence sequencia = new Sequence(Sequence.PPQ, TICKS_POR_SEMINIMA);
        Track faixa = sequencia.createTrack();

        // Andamento gravado na própria sequência (se mantém a cada repetição do loop)
        int microsPorSeminima = Math.round(60_000_000f / ANDAMENTO_BPM);
        byte[] dadosTempo = { (byte) (microsPorSeminima >> 16), (byte) (microsPorSeminima >> 8), (byte) microsPorSeminima };
        faixa.add(new MidiEvent(new MetaMessage(0x51, dadosTempo, 3), 0));

        // Canal 0: melodia (onda quadrada), canal 1: baixo, canal 9: bateria
        configurarCanal(faixa, 0, 80, 60);
        configurarCanal(faixa, 1, 38, 75);
        configurarCanal(faixa, 9, 0, 55);

        for (int i = 0; i < MELODIA.length; i++) {
            if (MELODIA[i] != 0) adicionarNota(faixa, 0, MELODIA[i], 80, i * COLCHEIA, COLCHEIA);
        }

        int compasso = 4 * TICKS_POR_SEMINIMA;
        for (int c = 0; c < BAIXO.length; c++) {
            long inicio = (long) c * compasso;
            int raiz = BAIXO[c];
            int[] linha = { raiz, raiz + 7, raiz + 12, raiz + 7 };
            for (int t = 0; t < 4; t++) {
                adicionarNota(faixa, 1, linha[t], 90, inicio + t * TICKS_POR_SEMINIMA, TICKS_POR_SEMINIMA);
            }
            for (int t = 0; t < 8; t++) {
                long tick = inicio + t * COLCHEIA;
                adicionarNota(faixa, 9, 42, 35, tick, 1);                               // chimbal
                if (t % 4 == 0) adicionarNota(faixa, 9, 36, 70, tick, 1);               // bumbo
                if (t % 4 == 2) adicionarNota(faixa, 9, 38, 45, tick, 1);               // caixa
            }
        }
        return sequencia;
    }

    private void configurarCanal(Track faixa, int canal, int instrumento, int volume) throws Exception {
        faixa.add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE, canal, instrumento, 0), 0));
        faixa.add(new MidiEvent(new ShortMessage(ShortMessage.CONTROL_CHANGE, canal, 7, volume), 0));
    }

    private void adicionarNota(Track faixa, int canal, int nota, int intensidade, long inicio, long duracao) throws Exception {
        faixa.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_ON, canal, nota, intensidade), inicio));
        faixa.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_OFF, canal, nota, 0), inicio + duracao));
    }
}
