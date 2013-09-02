cd D:\PhDWork\Jspace\wirelessCharging\test\data;


    C = load('small.txt');
    N = C(1:4,1);
    RB = C(1:4,2);
    h=plot(N,RB,'-dr');
    set(h,'MarkerSize',12);
    
    
    set(gcf,'Position',[1 1 600 400]);
    xlabel('Network Size');
    ylabel('Charging Throughput');
    hold on;
    
    
    RB = C(1:4,3);
    h=plot(N,RB,'-ob');
    set(h,'MarkerSize',12);
    

    RB = C(1:4,4);
    h=plot(N,RB,'-sk');
    set(h,'MarkerSize',12);
    
%     C = load('disOne-benefitgain-tour.txt');
%     RB = C(:,2);
%     h=plot(N,RB,'-^m');
%     set(h,'MarkerSize',12);
    
%     C = load('disTimTra-benefitgain-tour.txt');
%     RB = C(:,2);
%     h=plot(N,RB,'-xm');
%     set(h,'Color',[0.113 0.020 0.025],'MarkerEdgeColor',[0.113 0.020 0.025],'MarkerSize',12);
%     set(h,'MarkerSize',12);
    
%     C = load('disTra-utilitygain-tour.txt');
%     RB = C(:,4);
%     E(:,5)=RB;
%     h=plot(N,RB,'-pm');
%     set(h,'MarkerSize',12);
%     set(h,'Color',[0.3 0.3 0.025],'MarkerEdgeColor',[0.3 0.3 0.025],'MarkerSize',12);
    
    
    
    axis([10 25 0 20]);
    legend('Online\_SPT','Online\_K\_Cluster','Offline\_Appro',2);
   % legend('Impro\_Max\_Throu','Max\_Throu','Random\_Throu','Dis\_NSTT\_Throu','Dis\_TT\_Throu','Dis\_T\_Throu',2);
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
    
% fid=fopen('D:\PhDWork\Jspace\Mobilesink\test\xmgracedata\performance-goodput-T800.txt','w');%д���ļ�·��
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
