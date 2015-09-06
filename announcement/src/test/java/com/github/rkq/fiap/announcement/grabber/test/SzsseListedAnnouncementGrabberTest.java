package com.github.rkq.fiap.announcement.grabber.test;

import com.github.rkq.fiap.announcement.grabber.SzsseListedAnnouncementGrabber;
import com.github.rkq.fiap.announcement.model.ListedAnnouncement;
import org.junit.Test;

import java.util.List;

/**
 * Created by rick on 9/4/15.
 */
public class SzsseListedAnnouncementGrabberTest {
    @Test
    public void testGetLatest() {
        try {
            SzsseListedAnnouncementGrabber grabber = new SzsseListedAnnouncementGrabber();
            List<ListedAnnouncement> announcements = grabber.getLatest();
            for (ListedAnnouncement announcement : announcements) {
                System.out.println(announcement.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
