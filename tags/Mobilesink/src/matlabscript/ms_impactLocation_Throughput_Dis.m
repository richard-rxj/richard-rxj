cd D:\PhDWork\Jspace\Mobilesink\test\new\ImpactLocation\T800;

    %v = strcat(strcat('running_',int2str(i)),'.txt');
    Size=[100,200,300,400,500,600];
    C = load('disTra-benefit-location-20.txt');
    N = C(:,1);
    E(:,1)=N;
    [m,n]=size(N);
    RB = C(:,2);
    E(:,2)=RB;
    %subplot(1,2,1);
    %plot(N,CB,'-*r',N,WB,':pb');
    h=plot(N,RB,'-dr');
    set(h,'MarkerSize',12);
    set(gcf,'Position',[1 1 600 400]);
    %legend('Garg and K.','TPath',2);
    
    xlabel('Network Size');
    %ylabel('total flow per second(byte)');
    ylabel('Network Throughput Ratio');
    %title('Flow Comparison');
    
    hold on;
    
%     C = load('disTra-benefitgain-tour-T100.txt');
%     RB = C(:,2);
%     plot(N,RB,'-ok');
    
%     C = load('linear-weight-10.txt');
%     RB = C(:,2);
%     plot(N,RB,'-+g');
    C = load('disTra-benefit-location-40.txt');
    RB = C(:,2);
    E(:,3)=RB;
    h=plot(N,RB,'-om');
    set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerSize',12);
    %set(h,'MarkerSize',10);
    %set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerFaceColor',[0.113 0.020 0.025],'MarkerSize',10);



    
    C = load('disTra-benefit-location-60.txt');
    RB = C(:,2);
    E(:,4)=RB;
    h=plot(N,RB,'-pb');
    set(h,'MarkerSize',12);
%     C = load('linear-weight-30.txt');
%     RB = C(:,2);
%     plot(N,RB,'-pr');
    
    C = load('disTra-benefit-location-80.txt');
    RB = C(:,2);
    E(:,5)=RB;
    h=plot(N,RB,'-sk');
    set(h,'MarkerSize',12);
%     C = load('linear-weight-50.txt');
%     RB = C(:,2);
%     plot(N,RB,'-sr');
    
    C = load('disTra-benefit-location-100.txt');
    RB = C(:,2);
    E(:,6)=RB;
    h=plot(N,RB,'-^m');
    set(h,'MarkerSize',12);
%      C = load('linear-weight-70.txt');
%     RB = C(:,2);
%     plot(N,RB,'-<b');
    
%     C = load('disTra-benefitgain-tour-T6400.txt');
%     RB = C(:,2);
%     E(:,7)=RB;
%     h=plot(N,RB,'-xm');
%     set(h,'Color',[0.3 0.3 0.025],'MarkerEdgeColor',[0.3 0.3 0.025],'MarkerSize',12);
    
    
%     C = load('linear-weight-90.txt');
%     RB = C(:,2);
%     plot(N,RB,'-xr');
    
     axis([100,600,0,0.1]);  
    
    legend('|S| = 20','|S| = 40','|S| = 60','|S| = 80','|S| = 100',2);

%     legend('\lambda =0','\lambda =0.05','\lambda =0.1','\lambda =0.2','\lambda =0.3','\lambda =0.4','\lambda =0.5','\lambda =0.6','\lambda =0.7','\lambda =0.8','\lambda =0.9','\lambda =1',2);
    %
    %for t=(1:1:m-1)
   %     str  = [ 'PRatio:',num2str(RB(t)*100),'%' ]; 
   %     text(N(t),WB(t),['\leftarrow',str]);
   % end
   % str  = [ 'PRatio:',num2str(RB(m)*100),'%' ]; 
   % text(N(m)-24,WB(m),[str,'\rightarrow']);
    %
   % set(gcf, 'PaperPositionMode', 'manual');
   % set(gcf, 'PaperUnits', 'inches');
   % set(gcf, 'PaperPosition', [2 1 4 2]);
    set(gca,'fontsize',16,'fontname','Times');
    set(get(gca,'xlabel'),'fontsize',18);
    set(get(gca,'ylabel'),'fontsize',18);
    set(get(gca,'title'),'fontsize',18);
    set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',2);
    %v='pratio';
    %saveas(gcf,v,'eps');
    
    
%       fid=fopen('D:\PhDWork\Jspace\Mobilesink\test\xmgracedata\location-throughput-disTra.txt','w');%д���ļ�·��
% [m,n]=size(E); %��ȡ����Ĵ�С��pΪҪ����ľ���
% for i=1:1:m
%   for j=1:1:n
%      if j==n %���һ�еĸ����ﵽn�����У�����ո�
%         fprintf(fid,'%6.6f\n',E(i,j));
%     else
%        fprintf(fid,'%6.6f\t',E(i,j));
%     end
%   end
% end
%   fclose(fid); 
