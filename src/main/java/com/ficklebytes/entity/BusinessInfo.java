package com.ficklebytes.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "business_info")
public class BusinessInfo  implements Serializable{

		@Id
		@Column(name = "id")
	    private Integer id;
	    
	    @Column(name = "home_video_url")
	    private String homeVideoUrl;
	    
	    @Column(name = "user_guide_video_url")
	    private String userGuideVideoUrl;
	    
	    @Column(name = "user_guide_image_url")
	    private String userGuideImageUrl;
	    
	    @Column(name = "times_of_work")
	    private String timesOfWork;

	    @Column(name = "email")
	    private String email;
	    
	    @Column(name = "whatsapp_number")
	    private String whatsappNumber;
	    
	    @Column(name = "mobile_number")
	    private String mobileNumber;
	    
	    @Column(name = "viber_number")
	    private String viberNumber;
	    
	    @Column(name = "tel_number")
	    private String telNumber;

}
