package com.advertisement.common.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.jim2mov.core.DefaultMovieInfoProvider;
import org.jim2mov.core.ImageProvider;
import org.jim2mov.core.Jim2Mov;
import org.jim2mov.core.MovieInfoProvider;
import org.jim2mov.core.MovieSaveException;
import org.jim2mov.utils.MovieUtils;

/**
 * 图片与视频转换工具类
 * 
 * @author Administrator
 */
public class PicToAviUtil {

	/**
	 * 
	 * @param jpgs
	 *            需要生成视频的图片列表
	 * @param aviFileName
	 *            文件名称,只有名称,默认在本工程根路径下生成,要指定路径,需要在路径前加"file:",
	 * @param fps
	 *            每秒帧数
	 * @return
	 */
	public static boolean convertPicToAvi(List<File> jpgs, String aviFileName, float fps) {
		// 额外添加一张图片,解决最后一帧无法播放的问题
		final List<File> newJpgs = new ArrayList<>();
		newJpgs.addAll(jpgs);
		newJpgs.add(jpgs.get(0));
		// 生成视频的名称
		DefaultMovieInfoProvider dmip = new DefaultMovieInfoProvider(aviFileName);
		// 设置每秒帧数
		dmip.setFPS(fps > 0 ? fps : 1); // 如果未设置，默认为1
		// 设置总帧数
		dmip.setNumberOfFrames(newJpgs.size());
		// 设置视频宽和高（最好与图片宽高保持一直）
		BufferedImage image = null;
		try {
			image = ImageIO.read(jpgs.get(0));
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		dmip.setMWidth(image.getWidth());
		dmip.setMHeight(image.getHeight());

		try {
			Jim2Mov jim2Mov = new Jim2Mov(new ImageProvider() {
				public byte[] getImage(int frame) {
					try {
						// 设置压缩比
						return MovieUtils.convertImageToJPEG((newJpgs.get(frame)), 1.0f);
					} catch (IOException e) {
						System.err.println(e);
					}
					return null;
				}
			}, dmip, null);
			jim2Mov.saveMovie(MovieInfoProvider.TYPE_QUICKTIME_JPEG);
		} catch (MovieSaveException e) {
			System.err.println(e);
			System.exit(1);
		}

		return true;
	}

	public static boolean Pic2Mp4(List<File> jpgs, String saveMp4name, double fps) {
		//手动复制最后一张图片
		List<File> newJpgs=new ArrayList<File>();
		newJpgs.addAll(jpgs);
		newJpgs.add(jpgs.get(jpgs.size()-1));
		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(saveMp4name, 960,540);// 宽高必须是偶数你敢信?
		try {
			recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // 28
			// recorder.setVideoCodec(avcodec.AV_CODEC_ID_FLV1); // 28
			// recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4); // 13
			recorder.setFormat("mp4");
			// recorder.setFormat("mov,mp4,m4a,3gp,3g2,mj2,h264,ogg,MPEG4");
			recorder.setFrameRate(fps);
			recorder.setPixelFormat(0); // yuv420p
			recorder.start();
			Java2DFrameConverter java2dFrameConverter = new Java2DFrameConverter();

			// 循环所有图片
			for (File file : newJpgs) {
				BufferedImage img = ImageIO.read(file);
				recorder.record(java2dFrameConverter.convert(img));
				img.flush();
			}
			
			recorder.stop();
			recorder.release();
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}finally{
			if (recorder!=null) {
				try {
					recorder.stop();
					recorder.release();
				} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}

	/**
	 * main
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
//		String jpgDirPath = "C:/111"; // jpg文件夹路径
//		String aviFileName = "test00.mp4"; // 生成的avi视频文件名（生成路径为本工程）
//		float fps = 0.5f; // 每秒播放的帧数
//		int mWidth = 697; // 视频的宽度
//		int mHeight = 486; // 视频的高度
//		File[] jpgs = new File(jpgDirPath).listFiles();
//		List<File> jpgList = Arrays.asList(jpgs);
//		PicToAviUtil.convertPicToAvi(jpgList, aviFileName, fps);
//		System.out.println("end");
	}

}