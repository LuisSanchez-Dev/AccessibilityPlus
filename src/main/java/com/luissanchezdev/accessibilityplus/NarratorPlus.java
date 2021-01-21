package com.luissanchezdev.accessibilityplus;

import java.util.Locale;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

import com.sun.jna.Library;
import com.sun.jna.Native;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class NarratorPlus {
    private interface NVDA extends Library {
        public int nvdaController_speakText(char[] text);

        public int nvdaController_cancelSpeech();
    }

    public String lastText = "";
    public String prefixAmount = "";
    public Block lastBlock = null;
    public BlockPos lastBlockPos = null;
    private static NarratorPlus instance;
    private NVDA nvda;
    private SynthesizerModeDesc desc;
    private Synthesizer synthesizer;
    private Voice voice;

    public NarratorPlus() {
        instance = this;
        String osName = System.getProperty("os.name").toLowerCase(java.util.Locale.ROOT);
        if (osName.contains("win")) {
            try {
                this.nvda = (NVDA) Native.loadLibrary("nvdaControllerClient64", NVDA.class);
            } catch (java.lang.UnsatisfiedLinkError e64) {
                try {
                    this.nvda = (NVDA) Native.loadLibrary("nvdaControllerClient32", NVDA.class);
                } catch (java.lang.UnsatisfiedLinkError e32) {
                } finally {
                }
            } finally {
            }
        } else {
            if (desc == null) {
                try {
                    System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
                    desc = new SynthesizerModeDesc(Locale.US);
                    Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
                    synthesizer = Central.createSynthesizer(desc);
                    synthesizer.allocate();
                    synthesizer.resume();
                    SynthesizerModeDesc smd = (SynthesizerModeDesc) synthesizer.getEngineModeDesc();
                    Voice[] voices = smd.getVoices();
                    for (Voice voice1 : voices) {
                        if (voice1.getName().equals("kevin16")) {
                            voice = voice1;
                            break;
                        }
                    }
                    synthesizer.getSynthesizerProperties().setVoice(voice);
                    synthesizer.getSynthesizerProperties().setSpeakingRate(4*synthesizer.getSynthesizerProperties().getSpeakingRate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isNVDALoaded() {
        return instance.nvda != null;
    }

    public static void narrate(String text) {
        if (instance.nvda != null) {
            instance.nvda.nvdaController_cancelSpeech();
            char[] ch = new char[text.length() + 1]; // Last character must be null so NVDA decodes the text correctly
            for (int i = 0; i < text.length(); i++) {
                ch[i] = text.charAt(i);
            }

            instance.nvda.nvdaController_speakText(ch);
        } else if (instance.voice != null) {
            new Thread(()->{
                instance.synthesizer.cancel();
                instance.synthesizer.speakPlainText(text, null);
            }).start();
        }else {
            NarratorManager.INSTANCE.narrate(text);
        }
    }

    public static NarratorPlus getInstance() {
        return instance;
    }
}
